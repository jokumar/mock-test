package com.workday.addressbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StandardBook implements BookExt {
	protected final Map<Name, Set<Target>> addresses;

	public StandardBook() {
		super();
		addresses = new HashMap<Name, Set<Target>>();
	}

	@Override
	public boolean add(Name name, Target target) {
		Set<Target> targets = addresses.get(name);
		// Added
		Preconditions.checkArgument(!(name instanceof Alias && targets != null && !targets.contains(target)),
				"If the name argument is of Alias type, then argument target should not be mapped more than once");
		// Added
		Preconditions.checkArgument(!(target instanceof Name && addresses.get(target) == null),
				"If the target argement is of Name Type, then the address book should have that name");

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

			// Added
			if (name instanceof Group) {
				Preconditions.checkArgument(!(name instanceof Group && targets.size() == 1),
						"If the name argument is of Group type , the target argument should not be the final member among the targets ");
			}
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
				} else {
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
			sb.append(NEW_LINE).append(NEW_INDENT).append(addr.getKey()).append(" => [");
			boolean first = true;
			for (Target t : addr.getValue()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(t);
			}
			sb.append("]");
		}
		sb.append(NEW_LINE).append("}EndBook");
		return sb.toString();
	}

	@Override
	public Iterator<Name> getNames(boolean sortAsc) {
		List<Name> sortedKeys = new ArrayList<Name>(addresses.keySet());
		Comparator<Name> compareByName = (Name o1, Name o2) -> (sortAsc) ? 
				o1.getValue().compareTo(o2.getValue()) : 
					o2.getValue().compareTo(o1.getValue());
		Collections.sort(sortedKeys, compareByName);
		return sortedKeys.iterator();
	}

	@Override
	public Set<Name> lookup(Address address) {
		// TODO Auto-generated method stub
		return null;
	}

}
