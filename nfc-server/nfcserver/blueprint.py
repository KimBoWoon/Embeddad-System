# -*- coding: utf-8 -*-
from flask import Blueprint

nfc = Blueprint('nfc', __name__, template_folder='./templates', static_folder='./static')
