# -*- coding: utf-8 -*-
from sqlalchemy import Column, Integer, String
from nfcserver.db import Base


class User(Base):
    __tablename__ = 'users'

    num = Column(Integer, autoincrement=True)
    name = Column(String(20), nullable=False)
    nfcid = Column(String(50), nullable=False, primary_key=True)

    def __init__(self, name, nfcid):
        self.name = name
        self.nfcid = nfcid
