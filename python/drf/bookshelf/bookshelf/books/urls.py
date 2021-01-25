from django.urls import path
from .views import BookListCreateAPIView, BookDetailAPIView

app_name = 'books'

urlpatterns = [
    path('', BookListCreateAPIView.as_view(), name="list"),
    path('<int:pk>/', BookDetailAPIView.as_view(), name="detail"),
]
