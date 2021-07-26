from rest_framework import serializers
from .models import Users

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Users
        fields = ['user_name', 'user_id', 'user_pw']
