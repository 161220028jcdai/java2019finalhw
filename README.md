# JAVA大作业——葫芦娃VS蝎子精  

# 一、游戏简介  
葫芦娃、老爷爷、蝎子精、蛇精和妖怪小兵在二维战场上自由移动并且相互为敌
七个兄弟和老爷爷在空间的左侧站队；
妖精（蛇精、蝎子精、小喽啰）在空间右侧站队；
按空格键时所有生物体线程执行start()，向敌方前进；
当某个生物体于敌方相遇时，选取一个概率决定双方生死，死者留下实体，生者寻找下一个敌人攻击；
某一方生物全部死亡时，结束；
游戏操作：开始界面进行鼠标点击，开始游戏以后按空格开始游戏。其他的操作游戏里有说明。  
 
# 二、游戏效果  
开始界面如下。目前实现的功能有开始游戏、历史回放、关于游戏的说明、退出游戏。  
！[开始界面](https://img-blog.csdnimg.cn/20181231213848336.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3NzU2MTg3,size_16,color_FFFFFF,t_70)  
  
点击开始游戏即可进入战场，进入以后按空格键开始游戏，我们模拟一次战斗  
![战斗模拟](https://img-blog.csdnimg.cn/20181231214109403.gif)  

由于敌对生物体相碰时谁死谁活是公平随机的，这把葫芦娃运气不好被妖怪打败了！
此时战场上尸横遍野，你可以按空格键返回开始界面或者按S保存这次对战或者按L加载一次存档回放历史对战。  

# 三、设计思路和过程  
在战斗中出现的所有“东西”都有一个类对其进行包装。  

## 1、creature生物体  
![Creature结构](https://img-blog.csdnimg.cn/2018123122201812.png)  
Creature.java中定义了creature这一接口，因为游戏里这些角色有许多共性所以令他们都继承自Creature，以复用这些方法。不太一样的地方是葫芦娃和爷爷是一个阵营的，怪物属于另一个阵营monster。  

## 2、field战场和position坐标位置  
![field结构](https://img-blog.csdnimg.cn/20181231224137724.png)  
Position类作为辅助类用来表示角色在地图上的位置坐标（x，y）。field里调用了position类来控制生物体的位置同时利用线程系统来进行角色的行动。Creature类含有Runnable接口，通过调用run()方法进行线程的运行。每个角色就是一个线程。在角色run之后，进行一个直到角色死亡或者游戏结束才会停止的循环  

## 3、frame窗口界面  
![frame结构](https://img-blog.csdnimg.cn/20181231225229748.png)  
animation里设置定时器延时来实现动画的感觉。  
controller里面用来图像化上述所有的信息。与一个FXML文件绑定，从中读取UI界面的初始控件并对界面进行各种操作。在界面上贴图片使用Canvas画布控件，用内部方法实时更新战场信息，即将各个角色图片画上去。  
filemodel用来保存战斗导出到一个文件中去。  

## 4、stratagem阵型  
这里面直接根据阵型来设置初始妖精的坐标位置分布格局，实现了开局阵型，基本同之前第二第三次小作业时那样实现。  

# 四、面向对象设计思路
在本次实验中明确体现了以下的面向对象设计思想和方法：  

面向对象设计原则：在field上所有的物体，都以一个生物体对象来表示，每个生物体有自己的属性和行为，自己在地图上运行战斗并作出各种反应（函数的调用）；  
SRP单一职责原则：大部分类仅提供一类功能，例如表示生物体的Creature类及其子类均只会进行生物的基本操作；  
OCP开闭原则：举Creature为例，其各个继承子类均不能修改它的内部功能，但可以根据类型的不同扩展公共接口；  
观察者模式：Javafx使用观察者模式监控控件，例如按键的点击、键盘事件的监听等；  
