package course_management_swing_ui.models;

import course_management_swing_ui.util.Safe;
import course_management_swing_ui.util.exceptions.NotPossibleException;

public class CompulsoryModule extends Module {
    /**
     * @modifies idCountMap
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < <"M" + semester * 100 + idCount> , name, semester, COMPULSORY>
     * else
     * 	throw NotPossibleException</pre>
     */
    public CompulsoryModule(String name, int semester, int credits) throws NotPossibleException {
        super(name, semester, credits, ModuleType.COMPULSORY);
    }

    /**
     * @requires this.code was fetched from the database
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < code, name, semester, COMPULSORY>
     * else
     * 	throw NotPossibleException</pre>
     */
    @Safe
    public CompulsoryModule(String code, String name, int semester, int credits) throws NotPossibleException {
        super(code, name, semester, credits, ModuleType.COMPULSORY);
    }
}
