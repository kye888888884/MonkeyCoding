import cv2
from cv2 import QRCodeDetector

file_path = "./sample.jpg"
img = cv2.imread(file_path)
qr = QRCodeDetector()
data, bbox, straight_qrcode = qr.detectAndDecode(img)
print(data)