package io.github.wordandahalf.blueprint.environment;

import com.github.andrewoma.dexx.collection.Pair;
import io.github.wordandahalf.blueprint.asm.BlueprintContext;
import vlsi.utils.CompactHashMap;

import java.util.List;

//TODO: Proper JavaDocs
public class BlueprintManager {
    private  static CompactHashMap<Pair<String, String>, List<BlueprintContext>> contexts = new CompactHashMap<>();

    public static void add(BlueprintContext context)
    {

    }

    public static void remove(Pair<String, String> sourceAndTarget, BlueprintContext context)
    {

    }
}
