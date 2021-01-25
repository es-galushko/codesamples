import json
from datetime import datetime
from django.contrib.auth.models import User
from django.urls import reverse

from rest_framework.authtoken.models import Token
from rest_framework.test import APITestCase
from rest_framework import status
from .models import Book
from .serializers import BookSerializer


LEVIATHAN_WAKES = {
    "name": "Leviathan Wakes",
    "author": "James S.A. Corey",
    "date_of_writing": datetime(2011, 6, 15)
}


CALIBANS_WAR = {
    "name": "Caliban’s War",
    "author": "James S.A. Corey",
    "date_of_writing": datetime(2012, 6, 26)
}


class BookListCreateAPIViewTestCase(APITestCase):
    url = reverse("books:list")

    def setUp(self):
        self.username = "john"
        self.email = "john@snow.com"
        self.password = "you_know_nothing"
        self.user = User.objects.create_user(self.username, self.email, self.password)
        self.token = Token.objects.create(user=self.user)
        self.api_authentication()

    def api_authentication(self):
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + self.token.key)

    def test_create_book(self):
        response = self.client.post(self.url, LEVIATHAN_WAKES)
        self.assertEqual(status.HTTP_201_CREATED, response.status_code)

    def test_user_books(self):
        """
        Test to verify user books list
        """
        Book.objects.create(user=self.user, **LEVIATHAN_WAKES)
        response = self.client.get(self.url)
        self.assertTrue(len(json.loads(response.content)) == Book.objects.count())


class BookDetailAPIViewTestCase(APITestCase):

    def setUp(self):
        self.username = "john"
        self.email = "john@snow.com"
        self.password = "you_know_nothing"
        self.user = User.objects.create_user(self.username, self.email, self.password)
        self.book = Book.objects.create(user=self.user, **LEVIATHAN_WAKES)
        self.url = reverse("books:detail", kwargs={"pk": self.book.pk})
        self.token = Token.objects.create(user=self.user)
        self.api_authentication()

    def api_authentication(self):
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + self.token.key)

    def test_book_object_bundle(self):
        """
        Test to verify book object bundle
        """
        response = self.client.get(self.url)
        self.assertEqual(status.HTTP_200_OK, response.status_code)

        book_serializer_data = BookSerializer(instance=self.book).data
        response_data = json.loads(response.content)
        self.assertEqual(book_serializer_data, response_data)

    def test_book_object_update_authorization(self):
        """
            Test to verify that put call with different user token
        """
        new_user = User.objects.create_user("newuser", "new@user.com", "newpass")
        new_token = Token.objects.create(user=new_user)
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + new_token.key)

        # HTTP PUT
        response = self.client.put(self.url, CALIBANS_WAR)
        self.assertEqual(status.HTTP_403_FORBIDDEN, response.status_code)

        # HTTP PATCH
        response = self.client.patch(self.url, CALIBANS_WAR)
        self.assertEqual(status.HTTP_403_FORBIDDEN, response.status_code)

    def test_book_object_update(self):
        response = self.client.put(self.url, CALIBANS_WAR)
        response_data = json.loads(response.content)
        book = Book.objects.get(id=self.book.id)
        self.assertEqual(response_data.get("name"), book.name)

    def test_book_object_partial_update(self):
        response = self.client.patch(self.url, {"author": "Daniel Abraham"})
        response_data = json.loads(response.content)
        book = Book.objects.get(id=self.book.id)
        self.assertEqual(response_data.get("author"), book.author)

    def test_book_object_delete_authorization(self):
        """
            Test to verify that delete call with different user token
        """
        new_user = User.objects.create_user("newuser", "new@user.com", "newpass")
        new_token = Token.objects.create(user=new_user)
        self.client.credentials(HTTP_AUTHORIZATION='Token ' + new_token.key)
        response = self.client.delete(self.url)
        self.assertEqual(status.HTTP_403_FORBIDDEN, response.status_code)

    def test_book_object_delete(self):
        response = self.client.delete(self.url)
        self.assertEqual(status.HTTP_204_NO_CONTENT, response.status_code)
