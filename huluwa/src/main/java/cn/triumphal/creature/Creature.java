package cn.triumphal.creature;

import cn.triumphal.field.Position;
import javafx.scene.image.Image;

//生物体类接口继承Runnable实现多线程

public interface Creature extends Runnable{
	//生物体在画布上展示的当前状态的图片（生或死）
    Image report();
    //生物体“走”到的某个坐标位置
    void setPosition(Position position);
    //得到某个生物体的位置
    Position getPosition();
    //生物体的阵容
    int getSide();
    //生物体对象是否死亡
    Boolean isDead();
    //生物体对象死亡
    void setDead(Boolean dead);
    //获取生物体线程
    Thread getThread();
    void setThread(Thread t);
}
