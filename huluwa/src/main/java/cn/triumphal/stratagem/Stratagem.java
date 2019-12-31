package cn.triumphal.stratagem;

import cn.triumphal.creature.Creature;
import cn.triumphal.field.Field;
import java.util.ArrayList;
//阵型
public interface Stratagem {
    <Template extends Creature> void generate(int startRow, int startCol, ArrayList<Template> creatures, Field space);
}
