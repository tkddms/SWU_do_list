from rest_framework import serializers
from .models import sdiUser

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = sdiUser
        fields = ['username', 'password', 'email']

class UserSubjectSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = sdiUser
        fields = ['username', 'user_subjects']
