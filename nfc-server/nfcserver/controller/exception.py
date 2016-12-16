#-*- coding: utf-8 -*-
class NoneUserName(Exception):
    def __str__(self):
        return '등록되지 않은 사용자입니다'
