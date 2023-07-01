package course_management_swing_ui.util.entitiesMappers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
//        old way, same effect:  remember id of a dependency
//        Map<K, U> dependencyMap = new HashMap<>();
//        for (U dependency : dependencies) {
//            K id = getDepDep.apply(dependency);
//            dependencyMap.put(id, dependency);
//        }

        // remember id of a dependency
        Map<K, U> dependencyMap = dependencies.stream()
                .collect(Collectors.toMap(getDepDep, Function.identity()));

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
