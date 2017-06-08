new Vue({
    el: '#login_form',
    data: {
        login: '',
        password:'',
    },
    methods: {
        isValidLogin: function () {
            var valid = this.login.length > 0;
            return valid;
        },
        isValidPassword: function () {
            var valid = this.password.length > 0;
            return valid;
        },
        submitForm: function () {
            this.$http({url: '/', method: 'POST', data: {
                login: this.login,
                password: this.password
            }}).then( function (response) {
                location.reload();
            }, function (response) {
                alert('Error!:' + response.data);
            });
        }
    }
});