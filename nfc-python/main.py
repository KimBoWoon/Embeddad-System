import nfc

def connected(tag):
	print tag
	return False

clf = nfc.ContactlessFrontend('usb')
clf.connect(rdwr={'on-connect': connected})
