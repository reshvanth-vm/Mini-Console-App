package enums;

public enum EmployeeDesignation {
    ADMIN(1),
    MANAGER(2),
    TEAM_LEADER(3),
    MEMBER_TECHNICAL_STAFF(4),
    PROJECT_TRAINEE(5);

    private final int rank;

    private EmployeeDesignation(int rank) {
        this.rank = rank;
    };

    public int rank() {
        return this.rank;
    }

    public EmployeeDesignation getByRank(int rank) {
        for (EmployeeDesignation designation : values())
            if (designation.rank == rank)
                return designation;
        return null;
    }

    public EmployeeDesignation getNextHigherDesignation() {
        for (EmployeeDesignation designation : values())
            if (designation.rank == this.rank + 1)
                return designation;
        return null;
    }

    @Override
    public String toString() {
        return name()
                .charAt(0)
                + name().substring(1)
                        .toLowerCase()
                        .replace("_", " ");
    }
}
