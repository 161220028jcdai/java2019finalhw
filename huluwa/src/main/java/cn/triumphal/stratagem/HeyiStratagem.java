package cn.triumphal.stratagem;

import cn.triumphal.field.Field;
import cn.triumphal.creature.Creature;
import java.util.ArrayList;
//鹤翼阵
public class HeyiStratagem implements Stratagem {
    @Override
    public <Template extends Creature> void generate(int startRow, int startCol, ArrayList<Template> creatures, Field space) {
        for(int i = 0; i < creatures.size()/2; ++i)
            space.Add(startRow+i, startCol+i, creatures.get(i));
        for(int i = creatures.size()/2; i < creatures.size(); ++i)
            space.Add(startRow+creatures.size()-i-1, startCol+i, creatures.get(i));

    }
}
