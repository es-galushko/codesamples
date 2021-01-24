import json

from django.contrib.auth.models import User
from django.urls import reverse

from rest_framework.authtoken.models import Token
from rest_framework.test import APITestCase
from rest_framework import status


class UserRegistrationAPIViewTestCase(APITestCase):
    url = reverse("accounts:register")

    def test_invalid_password(self):
        """
        Test to verify that a post call with invalid passwords threaten as bad request
        """
        user_data = {
            "username": "testuser",
            "email": "test@testuser.com",
            "password": "password",
            "confirm_password": "INVALID_PASSWORD"
        }
        response = self.client.post(self.url, user_data)
        self.assertEqual(status.HTTP_400_BAD_REQUEST, response.status_code)

    def test_user_registration(self):
        """
        Test to verify that a post call with user valid data
        """
        user_data = {
            "username": "testuser",
            "email": "test@testuser.com",
            "password": "123123",
            "confirm_password": "123123"
        }
        response = self.client.post(self.url, user_data)
        self.assertEqual(status.HTTP_201_CREATED, response.status_code)
        self.assertTrue("token" in json.loads(response.content))

    def test_unique_username_validation(self):
        """
        Test to verify that a post call with already exists username
        """
        user_data_1 = {
            "username": "testuser",
            "email": "test@testuser.com",
            "password": "123123",
            "confirm_password": "123123"
        }
        response = self.client.post(self.url, user_data_1)
        self.assertEqual(status.HTTP_201_CREATED, response.status_code)

        user_data_2 = {
            "username": "testuser",
            "email": "test2@testuser.com",
            "password": "123123",
            "confirm_password": "123123"
        }
        response = self.client.post(self.url, user_data_2)
        self.assertEqual(status.HTTP_400_BAD_REQUEST, response.status_code)


class UserLoginAPIViewTestCase(APITestCase):
    url = reverse("accounts:login")

    def setUp(self):
        self.username = "john"
        self.email = "john@snow.com"
        self.password = "you_know_nothing"
        self.user = User.objects.create_user(self.username, self.email, self.password)

    def test_authentication_without_password(self):
        response = self.client.post(self.url, {"username": "snowman"})
        self.assertEqual(status.HTTP_400_BAD_REQUEST, response.status_code)

    def test_authentication_with_wrong_password(self):
        response = self.client.post(self.url, {"username": self.username, "password": "I_know"})
        self.assertEqual(status.HTTP_400_BAD_REQUEST, response.status_code)

    def test_authentication_with_valid_data(self):
        response = self.client.post(self.url, {"username": self.username, "password": self.password})
        self.assertEqual(status.HTTP_200_OK, response.status_code)
        self.assertTrue("auth_token" in json.loads(response.content))


class UserTokenAPIViewTestCase(APITestCase):
    def url(self, key):
        return reverse("accounts:token", kwargs={"key": key})

    def setUp(self):
        self.username = "john"
        self.email = "john@snow.com"
        self.password = "you_know_nothing"
        self.user = User.objects.create_user(self.username, self.email, self.password)
        self.token = Token.objects.create(user=self.user)
        self.api_authentication()

        self.user_2 = User.objects.create_user("mary", "mary@earth.com", "super_secret")
        self.token_2 = Token.objects.create(user=self.user_2)

    def tearDown(self):
        self.user.delete()
        self.token.delete()
        self.user_2.delete()
        self.token_2.delete()

    def api_authentication(self):
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + self.token.key)

    def test_delete_by_key(self):
        response = self.client.delete(self.url(self.token.key))
        self.assertEqual(status.HTTP_204_NO_CONTENT, response.status_code)
        self.assertFalse(Token.objects.filter(key=self.token.key).exists())

    def test_delete_current(self):
        response = self.client.delete(self.url('current'))
        self.assertEqual(status.HTTP_204_NO_CONTENT, response.status_code)
        self.assertFalse(Token.objects.filter(key=self.token.key).exists())

    def test_delete_unauthorized(self):
        response = self.client.delete(self.url(self.token_2.key))
        self.assertEqual(status.HTTP_404_NOT_FOUND, response.status_code)
        self.assertTrue(Token.objects.filter(key=self.token_2.key).exists())

    def test_get(self):
        # Test that unauthorized access returns 404
        response = self.client.get(self.url(self.token_2.key))
        self.assertEqual(status.HTTP_404_NOT_FOUND, response.status_code)

        for key in [self.token.key, 'current']:
            response = self.client.get(self.url(key))
            self.assertEqual(status.HTTP_200_OK, response.status_code)
            self.assertEqual(self.token.key, response.data['auth_token'])
            self.assertIn('created', response.data)
