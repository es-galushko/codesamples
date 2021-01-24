from django.urls import include, path
from django.contrib import admin


api_urls = [
    path('books/', include('books.urls')),
    path('', include('accounts.urls')),
]

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include(api_urls)),
]
