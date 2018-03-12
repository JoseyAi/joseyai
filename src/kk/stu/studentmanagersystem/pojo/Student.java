package kk.stu.studentmanagersystem.pojo;

public class Student {

	private String name;
	private String sex;
	private String minzu;
	private String id;
	private String birthday;
	private String phone;
	private String yuanxiao;
	private String xibie;
	private String banji;
	private String more;
	private boolean checked;

	public Student(String name, String sex, String minzu,
			String id, String birthday, String phone, String yuanxiao, String xibie, String banji, String more) {
		super();
		this.name = name;
		this.sex = sex;
		this.minzu = minzu;
		this.id = id;
		this.birthday = birthday;
		this.phone = phone;
		this.yuanxiao = yuanxiao;
		this.xibie = xibie;
		this.banji = banji;
		this.more = more;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMinzu() {
		return minzu;
	}

	public void setMinzu(String minzu) {
		this.minzu = minzu;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getYuanxiao() {
		return yuanxiao;
	}

	public void setYuanxiao(String yuanxiao) {
		this.yuanxiao = yuanxiao;
	}

	public String getXibie() {
		return xibie;
	}

	public void setXibie(String xibie) {
		this.xibie = xibie;
	}

	public String getBanji() {
		return banji;
	}

	public void setBanji(String banji) {
		this.banji = banji;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

}
