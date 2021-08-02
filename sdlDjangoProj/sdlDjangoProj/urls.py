from django.contrib import admin
from django.urls import path, include
from swudolist import views
# Wire up our API using automatic URL routing.
# Additionally, we include login URLs for the browsable API.
urlpatterns = [
    # 순서대로 관리자 - 유저 리스트 - 특정 유저 - 로그인
    path('admin/', admin.site.urls),
    path('users/', views.users_list),
    path('users/<int:pk>/', views.user),
    # path('login/', views.login),
    path('app_login/', views.app_login),
    path('app_register/', views.app_register),
    path('app_update/', views.app_update_subject),
    path('app_get_user/', views.app_get_user),
    path('app_logout/', views.app_logout),
    path('app_add_todolist/', views.app_add_toDoList),
    path('app_get_todolist/', views.app_get_toDoList),
    path('app_edit_post/', views.app_edit_post),
    path('app_get_posts/', views.app_get_posts),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]
