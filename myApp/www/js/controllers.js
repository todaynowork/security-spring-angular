angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('ChatsCtrl', function($scope, Chats) {
  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  };
})

.controller('ChatDetailCtrl', function($scope, $stateParams, Chats) {
  $scope.chat = Chats.get($stateParams.chatId);
})

.controller('AccountCtrl', function($scope,$http,$rootScope) {
  $scope.settings = {
    enableFriends: true
  };

  // user toke to authenticate 
  var headers = $rootScope.token ? {"x-auth-token" : $rootScope.token} : {};
  $http.get('http://9.110.214.53:8080/resource', {headers : headers}).then(function(response) {
      console.log(response);
    }, function() {
      console.log("error occured");
    });
})
.controller('SignInCtrl', function($scope, $state,$http,$rootScope) {
  var self = this;
  
  var authenticate = function(credentials, callback) {

    var headers = credentials ? {authorization : "Basic "
        + btoa(credentials.username + ":" + credentials.password)
    } : {};
    // please did not user localhost for mobile app
    $http.get('http://9.110.214.53:8080/user', {headers : headers}).then(function(response) {
      if (response.data.name) {

        //get token
        $rootScope.token = response.headers("x-auth-token");
        $rootScope.authenticated = true;
      } else {
        $rootScope.authenticated = false;
      }
      callback && callback();
    }, function() {
      $rootScope.authenticated = false;
      callback && callback();
    });
  }

  $scope.signIn = function(user) {
    authenticate(user, function() {
        if ($rootScope.authenticated) {
          $state.go('tab.dash');
          self.error = false;
        } else {
          $state.go('login');
          self.error = true;
        }
    });
    console.log('Sign-In', user);

  };
  
});
