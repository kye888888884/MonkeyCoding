import cv2
from itertools import product

W = 100
H = 100

def get_color(img, x, y):
    return img[y][x]

def get_binary(img, cx, cy):
    w = [cx - W / 4, cx + W / 4]
    h = [cy - H / 4, cy + H / 4]
    bin = []
    for y, x in product(h, w):
        c = get_color(img, int(x), int(y))
        bin.append(sum(c) / 3 < 128)
    return bin

pos = [(75, 280), (170, 280), (270, 280), (370, 280),
       (75, 380), (170, 380), (270, 380), (370, 380)]

file_path = "./sample.jpg"
img = cv2.imread(file_path)
for cx, cy in pos:
    print(get_binary(img, cx, cy))

