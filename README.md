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
![开始界面](https://img-blog.csdnimg.cn/20181231213848336.png)  
  
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
![frame结构](https://github.com/161220028jcdai/java2019finalhw/blob/master/frame.png)  
donghua里设置定时器延时来实现动画的感觉。  
controller里面用来图像化上述所有的信息。与一个FXML文件绑定，从中读取UI界面的初始控件并对界面进行各种操作。在界面上贴图片使用Canvas画布控件，用内部方法实时更新战场信息，即将各个角色图片画上去。  
filemodel用来保存战斗导出到一个文件中去。  
Sountracks利用MediaPlayer为游戏增加BGM音乐。

## 4、stratagem阵型  
这里面直接根据阵型来设置初始妖精的坐标位置分布格局，实现了开局阵型，基本同之前第二第三次小作业时那样实现。  

## 5、保存回放功能
思路是按观察者模式，每当画布布局产生变化时，线程把当前画布上的信息序列化成字符串，因为有96个坐标格子（坐标点）所以字符串有96位，每一行96位字符串用#和#\n隔开，用不同的字符表示每个坐标格（点）上的不同生物体，具体代码如下：
```
    //保存当前的情况到字符串
    public void saveCurrentField(){
        fieldDocument += '#';
        for(int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                String kind = creatures[i][j].getClass().getSimpleName();
                switch (kind){
                    case "Space":fieldDocument += '_'; continue;
                    case "Grandpa":fieldDocument += (creatures[i][j].isDead())?'H':'G';continue;
                    case "Huluwa":fieldDocument += ((Huluwa)creatures[i][j]).getNum();continue;
                    case "Snake":fieldDocument += (creatures[i][j].isDead())?'J':'K';continue;
                    case "Xiaolouluo":fieldDocument += (creatures[i][j].isDead())?'L':'Z';continue;
                    case "Xiezijing":fieldDocument += (creatures[i][j].isDead())?'X':'C';continue;
                    default:fieldDocument+='_';continue;
                }
            }
        }
        fieldDocument += '#';
        fieldDocument += '\n';
    }
```

然后要进行保存时，把这个字符串保存到以hlw为后缀的文件中  
保存文件的代码如下：
```
            else if(keyEvent.getCode() == KeyCode.S && Mode == 3) {
                HLWFileFilter filter = new HLWFileFilter();
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(filter);
                fc.setMultiSelectionEnabled(false);
                int result = fc.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (!file.getPath().endsWith(".hlw")) {
                        file = new File(file.getPath() + ".hlw");
                    }
                    //System.out.println("file path = " + file.getAbsolutePath());
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new FileModel().saveToFile(file.getAbsolutePath(), field.getFieldDocument());
                }
            }
```

回放功能即读取某个hlw文件然后按存入时的规则解析字符串序列打印到画布上。  

```
//从文件读取历史战斗
    public boolean getHistoryField(String filename, int step) {
        Reader reader;
        try{
            reader = new FileReader(filename);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        long skip = (sizeX * sizeY + 3 /*##R*/) * step;
        try {
            reader.skip(skip);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Boolean start = false;
        int cnt = -1; //第一个符号不计

        CleanField();

        while(true){
            try {
                char c = (char)(reader.read());
                if(start == false && c != '#')
                    return false;
                if(c == '#' && start == false)
                    start = true;
                else if(c == '#' && start == true)
                    break;
                else{
                    int posx = cnt / 8;
                    int posy = cnt % 8;
                    switch (c){
                        case 'I':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[0], Huluwa.SENIORITY.values()[0], this));creatures[posx][posy].setDead(true);break;
                        case 'O':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[1], Huluwa.SENIORITY.values()[1], this));creatures[posx][posy].setDead(true);break;
                        case 'P':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[2], Huluwa.SENIORITY.values()[2], this));creatures[posx][posy].setDead(true);break;
                        case 'A':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[3], Huluwa.SENIORITY.values()[3], this));creatures[posx][posy].setDead(true);break;
                        case 'S':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[4], Huluwa.SENIORITY.values()[4], this));creatures[posx][posy].setDead(true);break;
                        case 'D':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[5], Huluwa.SENIORITY.values()[5], this));creatures[posx][posy].setDead(true);break;
                        case 'F':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[6], Huluwa.SENIORITY.values()[6], this));creatures[posx][posy].setDead(true);break;
                        case 'H':Add(posx, posy, new Grandpa(this));creatures[posx][posy].setDead(true);break;
                        case 'J':Add(posx, posy, new Snake(this));creatures[posx][posy].setDead(true);break;
                        case 'L':Add(posx, posy, new Xiaolouluo(this));creatures[posx][posy].setDead(true);break;
                        case 'X':Add(posx, posy, new Xiezijing(this));creatures[posx][posy].setDead(true);break;
                        case 'Q':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[0], Huluwa.SENIORITY.values()[0], this));creatures[posx][posy].setDead(false);break;
                        case 'W':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[1], Huluwa.SENIORITY.values()[1], this));creatures[posx][posy].setDead(false);break;
                        case 'E':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[2], Huluwa.SENIORITY.values()[2], this));creatures[posx][posy].setDead(false);break;
                        case 'R':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[3], Huluwa.SENIORITY.values()[3], this));creatures[posx][posy].setDead(false);break;
                        case 'T':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[4], Huluwa.SENIORITY.values()[4], this));creatures[posx][posy].setDead(false);break;
                        case 'Y':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[5], Huluwa.SENIORITY.values()[5], this));creatures[posx][posy].setDead(false);break;
                        case 'U':Add(posx, posy, new Huluwa(Huluwa.COLOR.values()[6], Huluwa.SENIORITY.values()[6], this));creatures[posx][posy].setDead(false);break;
                        case 'G':Add(posx, posy, new Grandpa(this));creatures[posx][posy].setDead(false);break;
                        case 'K':Add(posx, posy, new Snake(this));creatures[posx][posy].setDead(false);break;
                        case 'Z':Add(posx, posy, new Xiaolouluo(this));creatures[posx][posy].setDead(false);break;
                        case 'C':Add(posx, posy, new Xiezijing(this));creatures[posx][posy].setDead(false);break;
                        default:break;
                    }
                }
                cnt++;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
```

```
//读取历史界面
        else if(Mode == 2){//load History
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                gameOver();
            else if(keyEvent.getCode() == KeyCode.SPACE) {
                if(!isRunning) {
                    isRunning = true;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (field.getHistoryField(historyFilename, Step++) == false) { //文件异常
                                gameOver();
                                this.cancel();
                            }
                            else {
                                display(field);
                                int wins = field.isGameOver();
                                if (wins != 0) {
                                    this.cancel();
                                    Mode = 4;
                                    cleanFieldPane();
                                    cleanDeathPane();
                                    if(wins == 1)
                                        field.showGoodGuyWin();
                                    else if(wins == -1)
                                        field.showBadGuyWin();
                                    display(field);
                                    isRunning = false;
                                }
                            }
                        }
                    }, 500, 500);
                }
            }
        }
```



# 四、面向对象设计思路
在本次实验中明确体现了以下的面向对象设计思想方法以及java中的各种机制：  

面向对象设计原则：在field上所有的物体，都以一个生物体对象来表示，每个生物体有自己的属性和行为，自己在地图上运行战斗并作出各种反应（函数的调用）；  
SRP单一职责原则：大部分类仅提供一类功能，例如表示生物体的Creature类及其子类均只会进行生物的基本操作；  
OCP开闭原则：举Creature为例，其各个继承子类均不能修改它的内部功能，但可以根据类型的不同扩展公共接口；  
观察者模式：Javafx使用观察者模式监控控件，例如按钮的点击、键盘事件的监听等；  
输入输出：保存字符串流并以hlw文件形式输出，输入hlw文件解析里面的字符串内容加载画布内容。
异常处理：通过try、catch捕获异常
```
            while(field.getIsDisplaying()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                    break;
                }
            }
```
注解：多处利用@Override - 检查该方法是否是重载方法。如果发现其父类，或者是引用的接口中并没有该方法时，会报编译错误。  


### PS：感谢两位老师和各位助教这一学期的辛苦工作和奉献，java世界充满魅力。
