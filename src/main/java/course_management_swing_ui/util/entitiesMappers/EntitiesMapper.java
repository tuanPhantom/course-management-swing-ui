package course_management_swing_ui.util.entitiesMappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EntitiesMapper {
    private static EntitiesMapper instance;

    private EntitiesMapper() {

    }

    static EntitiesMapper getInstance() {
        if (instance == null) {
            instance = new EntitiesMapper();
        }
        return instance;
    }

    @FunctionalInterface
    interface TwoParamInterface<T, U> {
        void performOperation(T param1, U param2);
    }

    public <T, U, K> void mapDependency(List<T> objects, Function<T, U> getObjDep, TwoParamInterface<T, U> setObjDep, List<U> dependencies, Function<U, K> getDepDep) {
        Map<K, U> dependencyMap = new HashMap<>();

        // remember id of a dependency
        for (U dependency : dependencies) {
            K id = getDepDep.apply(dependency);
            dependencyMap.put(id, dependency);
        }

        // for each object, if contains id in map, set dependency in map as its dependency.
        for (T object : objects) {
            U dependency = getObjDep.apply(object);
            K id = getDepDep.apply(dependency);
            U get = dependencyMap.get(id);
            if (get != null) {
                setObjDep.performOperation(object, get);
            }
        }
    }
}
