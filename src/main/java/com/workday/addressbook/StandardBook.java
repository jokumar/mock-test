package com.workday.addressbook;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StandardBook
    implements Book {
    protected final Map<Name, Set<Target>> addresses;

    public StandardBook() {
        super();
        addresses = new HashMap<Name, Set<Target>>();
    }

    @Override
    public boolean add(Name name, Target target) {
        Set<Target> targets = addresses.get(name);
        if (targets == null) {
            targets = new HashSet<Target>();
            addresses.put(name, targets);
        }
        return targets.add(target);
    }

    @Override
    public boolean delete(Name name, Target target) {
        Set<Target> targets = addresses.get(name);
        if (targets != null && targets.contains(target)) {
            if (targets.size() > 1) {
                return targets.remove(target);
            }
            return addresses.remove(name) != null;
        }
        return false;
    }

    @Override
    public Set<Address> lookup(Name name) {
        Preconditions.checkNotNull(name, "name");
        Set<Address> results = new HashSet<Address>();
        Set<Target> targets = addresses.get(name);
        if (targets != null) {
            for (Target target : targets) {
                if (target instanceof Address) {
                    results.add((Address) target);
                }
                else {
                    // resolve alias or group
                    results.addAll(lookup((Name) target));
                }
            }
        }
        return results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String NEW_INDENT = " ";
        String NEW_LINE = System.getProperty("line.separator");
        sb.append("{Book ");
        for (Map.Entry<Name, Set<Target>> addr : addresses.entrySet()) {
            sb.append(NEW_LINE).append(NEW_INDENT).append(addr.getKey()).append(
                    " => [");
            boolean first = true;
            for (Target t : addr.getValue()) {
                if (first) {
                    first = false;
                }
                else {
                    sb.append(", ");
                }
                sb.append(t);
            }
            sb.append("]");
        }
        sb.append(NEW_LINE).append("}EndBook");
        return sb.toString();
    }

}
