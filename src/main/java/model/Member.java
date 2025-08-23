package model;

public class Member {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String level; // 一般 / VIP
    private String memberCode;

    public Member() {}

    public Member(int id, String name, String phone, String email, String level, String memberCode) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.level = level;
        this.memberCode = memberCode;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getMemberCode() { return memberCode; }
    public void setMemberCode(String memberCode) { this.memberCode = memberCode; }

    public boolean isVIP() { return "VIP".equalsIgnoreCase(level); }
    @Override
    public String toString() {
        return name + " (" + memberCode + ")";
    }
}
