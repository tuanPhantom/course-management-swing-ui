package a2_1901040191.util.dto;

import a2_1901040191.model.ElectiveModule;
import a2_1901040191.model.Enrollment;
import a2_1901040191.model.Module;
import a2_1901040191.model.Student;
import a2_1901040191.util.ThreadPool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This helper class generates Vector of Object's Field as Data Transfer Object (DTO). This help us to build
 * different views from our domain models, allowing us to create other representations of the same domain but optimizing
 * them to the clients' needs without affecting our domain design
 */
public class DtoGenerator {
    /**
     * @requires fields!=null /\ type!=null
     * @effects <pre>
     *     recursively while type still has a parent class:
     *          add type of the current class to fields
     * </pre>
     */
    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        return fields;
    }

    /**
     * @requires o!=null
     * @effects return a list of all Fields in o
     */
    private static List<Field> getAllFields(Object o) {
        return getAllFields(new ArrayList<>(), o.getClass());
    }

    /**
     * @requires o!=null
     * @effects return the DTO of o by recognize all its Fields.
     */
    private synchronized static Vector<?> getDto(Object o) {
        Field[] fields = getAllFields(o).stream()
                .filter(f -> Arrays.stream(f.getDeclaredAnnotations()).anyMatch(a -> {
                    try {
                        return (a.annotationType().getSimpleName().equals(DisplayInTable.class.getSimpleName())
                                && !((boolean) (a.annotationType().getDeclaredMethod("skip").invoke(a))));
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                })).toArray(Field[]::new);

        Vector<Object> dataValues = Arrays.stream(fields).map(f -> {
            boolean changedAccess = false;
            try {
                if (!f.canAccess(o)) {
                    f.setAccessible(true);
                    changedAccess = true;
                }
                return f.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                if (changedAccess) {
                    f.setAccessible(false);
                }
            }
            return null;
        }).collect(Collectors.toCollection(Vector::new));

        dataValues.add(false);
        return dataValues;
    }

    /**
     * @requires objs!=null
     * @effects return a list contains the DTO(s) of each element in objs
     */
    public static Vector<Vector<?>> getDto(List<?> objs) {
        Vector<Vector<?>> results = new Vector<>();
        List<Callable<Vector<?>>> tasks = new ArrayList<>();
        objs.forEach(o -> tasks.add(() -> getDto(o)));

        try {
            ThreadPool.executor.invokeAll(tasks).forEach(f -> {
                try {
                    results.add(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * @requires m!=null
     * @effects return the DTO(s) of Module m
     */
    private static Vector<?> getDto_module(Module m) {
        if (m.getModuleType() == Module.ModuleType.COMPULSORY) {
            return new Vector<>(List.of(new Object[]{m.getCode(), m.getName(), m.getSemester(), m.getCredits(), m.getModuleType(), "", false}));
        } else {
            return new Vector<>(List.of(new Object[]{m.getCode(), m.getName(), m.getSemester(), m.getCredits(), m.getModuleType(), ((ElectiveModule) m).getDepartment(), false}));
        }
    }

    /**
     * @requires objs!=null
     * @effects return all the DTO(s) of each element in objs
     */
    public static Vector<Vector<?>> getDto_module(List<Module> objs) {
        Vector<Vector<?>> results = new Vector<>();
        objs.forEach(m -> results.add(getDto_module(m)));
        return results;
    }


    /**
     * @requires e!=null
     * @effects return the DTO(s) or initial Report View
     */
    private static Vector<?> getDto_initialReport(Enrollment e) {
        Student s = e.getStudent();
        Module m = e.getModule();
        return new Vector<>(List.of(new Object[]{e.getId(), s.getId(), s.getName(), m.getCode(), m.getName(), false}));
    }

    /**
     * @requires objs!=null
     * @effects return all the DTO(s) of each element in objs
     */
    public static Vector<Vector<?>> getDto_initialReport(List<Enrollment> objs) {
        Vector<Vector<?>> results = new Vector<>();
        objs.forEach(e -> results.add(getDto_initialReport(e)));
        return results;
    }

    /**
     * @requires e!=null
     * @effects return the DTO(s) or Assessment Report View
     */
    private static Vector<?> getDto_assessmentReport(Enrollment e) {
        Student s = e.getStudent();
        Module m = e.getModule();
        return new Vector<>(List.of(new Object[]{e.getId(), s.getId(), m.getCode(),
                e.getInternalMark(), e.getExaminationMark(), e.getFinalGrade(), false}));
    }

    /**
     * @requires objs!=null
     * @effects return all the DTO(s) of each element in objs
     */
    public static Vector<Vector<?>> getDto_assessmentReport(List<Enrollment> objs) {
        Vector<Vector<?>> results = new Vector<>();
        objs.forEach(e -> results.add(getDto_assessmentReport(e)));
        return results;
    }

//    test cases
//    public static void main(String[] args) throws NotPossibleException, InvalidArgumentException {
//        Module m = ModuleFactory.getInstance().createModule(Name.ModuleName.ATI, 2022, 3, Module.ModuleType.ELECTIVE, Name.DepartmentName.FIT);
//        System.out.println(getDto(m));
//
//        Module m1 = ModuleFactory.getInstance().createModule(Name.ModuleName.ATI, 2022, 3, Module.ModuleType.COMPULSORY, Name.DepartmentName.FIT);
//        System.out.println(getDto(m1));
//
//        Student s = new Student(2023, "Tuan", LocalDate.parse("1995-02-25"), "Hanoi", "sdfsdfs@gmail.com");
//        System.out.println(getDto(s));
//
//        Enrollment e = new Enrollment(5, s, m, 4.0, 9.5);
//        System.out.println(getDto(e));
//    }
}
