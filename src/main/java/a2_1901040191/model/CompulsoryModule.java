package a2_1901040191.model;

import a2_1901040191.util.Safe;
import a2_1901040191.util.exceptions.NotPossibleException;

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
