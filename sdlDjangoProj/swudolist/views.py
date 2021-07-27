from django.contrib import auth
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django.http import JsonResponse, HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from .models import Users
from .serializers import UserSerializer
# Create your views here.

@csrf_exempt
def users_list(request):
   #  Users 리스트 조회
   if request.method == 'GET':
       query_set = Users.objects.all()
       serializer = UserSerializer(query_set, many=True)
       return JsonResponse(serializer.data, safe=False)
   # Users 객체 생성
   elif request.method == 'POST':
       data = JSONParser().parse(request)
       serializer = UserSerializer(data=data)
       if serializer.is_valid():
           serializer.save()
           return JsonResponse(serializer.data, status=201)
       return JsonResponse(serializer.data, status=400)

@csrf_exempt
# 유저 객체 수정, 삭제, 조회
def user(request, pk):
    obj = Users.objects.get(pk=pk)

    if request.method == 'GET':
        serializer = UserSerializer(obj)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = UserSerializer(obj, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        obj.delete()
        return HttpResponse(status=204)

# @csrf_exempt
# # 로그인 - 웹 -> TEST (정상 작동)
# def login(request):
#     if request.method == 'POST':
#         data = JSONParser().parse(request)
#         target_id = data['user_id']
#         obj = Users.objects.get(user_id=target_id)
#
#         if data['user_name'] == obj.user_name:
#             return HttpResponse(status=200)
#         else:
#             return HttpResponse(status=400)

@csrf_exempt
# 로그인 - 앱
def app_login(request):
    if request.method == 'POST':
        print("리퀘스트 로그: " + str(request.body))
        id = request.POST.get('user_id', '')
        pw = request.POST.get('user_pw', '')
        print("id = " + id + " pw = " + pw)

        result = authenticate(username=id, password=pw)

        print(result)

        if result:
            print("successed")
            return JsonResponse({'code': '0000', 'msg': 'login success!'}, status=200)
        else:
            print("fail")
            # status=400 으로 하면 안드로이드 상에서 배제해버려서 결과가 나오지 않음.
            return JsonResponse({'code': '1001', 'msg': 'login failed'}, status=200)


def app_sign_up(request):
    if request.method == 'POST':
        print("리퀘스트 로그(sign_up): " + str(request.body))
        id = request.POST.get('user_r_id', '')
        pw = request.POST.get('user_r_pw', '')
        email = request.POST.get('user_r_email', '')

        user = User.objects.create_user(id, email, pw)
        result = authenticate(username=id, password=pw)

        if result:
            print("successed signin")
            auth.login(user)
            return JsonResponse({'code': '0000', 'msg': 'signin success!'}, status=200)
        else:
            # status=400 으로 하면 안드로이드 상에서 배제해버려서 결과가 나오지 않음.
            return JsonResponse({'code': '1001', 'msg': 'signin failed'}, status=200)
