# ch-android
chunhui android client

默认文件存放相关路径
/storage/emulated/0/Android/data/com.chunhuitech.reader/files/Documents/
mp3文件保存地址：
/storage/emulated/0/Android/data/com.chunhuitech.reader/files/Documents/1264/res/12

Android图标生成
File->New->Image Asset
Android 8.0应用图标适配&mipmap-anydpi-v26

图片资源要求：
两页合并一张  
尺寸 1080 x 810
宽度:1080像素
高度:810像素
水平分辨率:96dpi
垂直分辨率:96dpi
位深度:24


mp3音频播放位置计算方法:
以秒为单位，对应的分钟数乘以60，再加后面的秒数，即为开始时间点和结束时间点