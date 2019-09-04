# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse
from django.shortcuts import render
import urlparse
import os.path

url_list = []

def index(request):
    s = ''
    add_url(request)
    for entry in url_list:
        s += entry + '<br/>'

    return HttpResponse(s)

def add_url(request):
    path = request.path
    if '//' not in path:
        s = ''
        temp = ""
        while path != '/':
            temp = os.path.split(path)
            path = temp[0]
            if temp[1] != 'urlparts':
                s += temp[1] + '---'

        url_list.append(s)
