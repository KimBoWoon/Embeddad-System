# -*- coding: utf-8 -*-
from sqlalchemy import Column, Integer, String, DateTime
from nfcserver.db import Base
from datetime import datetime


class Access(Base):
    __tablename__ = 'access'

    num = Column(Integer, autoincrement=True, primary_key=True)
    name = Column(String(20), nullable=False)
    nfcid = Column(String(50), nullable=False)
    date = Column(DateTime, nullable=False)

    def __init__(self, name, nfcid):
        self.name = name
        self.nfcid = nfcid
        self.date = datetime.today().strftime("%Y-%m-%d %H:%M:%S")
