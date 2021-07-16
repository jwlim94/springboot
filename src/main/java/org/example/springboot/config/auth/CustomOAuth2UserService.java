package org.example.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.example.springboot.config.auth.dto.OAuthAttributes;
import org.example.springboot.config.auth.dto.SessionUser;
import org.example.springboot.domain.user.User;
import org.example.springboot.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;
  private final HttpSession httpSession; //PURPOSE: ?

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    //NOTE: 퍼레미터에 있는 userRequest 에는 SecurityConfig 클래스에서 .userInfoEndpoint() 를 통해 값이 넘겨진거 같다.
    String registrationId = userRequest.getClientRegistration().getRegistrationId(); //NOTE: "google"
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                    .getUserInfoEndpoint().getUserNameAttributeName(); //NOTE: google 에서는 "sub"

    //PURPOSE: clientId, clientSecret, registrationId, userNMameAttributeName, 등 의 정보를 가지고 oAuth2User 을 가져온다.
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    //PURPOSE: 가져온 oAuth2User 에서 attributes 를 가져와서 oAuthAttributes dto 클래스에 값을 넣는다. (name, email, picture, 등)
    OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    User user = saveOrUpdate(attributes);

    httpSession.setAttribute("user", new SessionUser(user));

//    System.out.println();
//    System.out.println(user.getRoleKey());

    return new DefaultOAuth2User(
      Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                            attributes.getAttributes(),
                            attributes.getNameAttributeKey());
  }

  //PURPOSE: register a new user with a ROLE_GUEST or update the existing user.
  private User saveOrUpdate(OAuthAttributes attributes) {
    User user = userRepository.findByEmail(attributes.getEmail())
                              .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                              .orElse(attributes.toEntity()); //NOTE: if the user does not exist, do register
    return userRepository.save(user);
  }
}
