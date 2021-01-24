from django.urls import path
from .views import (
    UserRegistrationAPIView,
    UserLoginAPIView,
    UserTokenAPIView
)

app_name = 'accounts'

urlpatterns = [
    path('users/', UserRegistrationAPIView.as_view(), name="register"),
    path('users/login/', UserLoginAPIView.as_view(), name="login"),
    path('tokens/<key>/', UserTokenAPIView.as_view(), name="token"),
]
