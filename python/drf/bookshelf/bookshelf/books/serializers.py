from django.contrib.auth.models import User
from rest_framework import serializers
from .models import Book


class BookUserSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = (
            "id",
            "username",
            "email",
            "date_joined"
        )


class BookSerializer(serializers.ModelSerializer):
    user = BookUserSerializer(read_only=True)

    class Meta:
        model = Book
        fields = (
            "user",
            "name",
            "author",
            "date_of_writing"
        )
