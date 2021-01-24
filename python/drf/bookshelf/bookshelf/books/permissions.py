from rest_framework.permissions import BasePermission


class UserIsOwnerBook(BasePermission):

    def has_object_permission(self, request, view, book):
        return request.user.id == book.user.id
