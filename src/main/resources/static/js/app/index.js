var index = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () { //PURPOSE: .on is a event handler
            _this.save();
        });
        $('#btn-update').on('click', function () {
            _this.update();
        });
        $('#btn-delete').on('click', function() {
            _this.del();
        });
    },
    save : function () {
        var data = {
            title: $('#title').val(), //PURPOSE: .val() is used to get the value from textarea, input, etc
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({ //PURPOSE: perform an asynchronous HTTP request
            type: 'POST',
            url: '/api/v1/posts', //NOTE: in which the API to call from the @Controller; 등록 API
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data) //PURPOSE: Convert a JavaScript object into a string. The result will be a string following the JSON notation.
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/'; //PURPOSE: 글 등록이 성공하면 메인페이지(/)로 이동한다.
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id, // 데이터베이스를 수정함
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function () {
            alert(JSON.stringify(error));
        });
    },
    del: function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function () {
            alert(JSON.stringify(error));
        });
    }
};

index.init();