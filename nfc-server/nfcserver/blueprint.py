# -*- coding: utf-8 -*-
from flask import Blueprint

rainbow = Blueprint('nfc', __name__, template_folder='./templates', static_folder='./static')
