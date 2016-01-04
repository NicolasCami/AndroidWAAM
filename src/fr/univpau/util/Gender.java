package fr.univpau.util;

public enum Gender {
	MALE(1),
	FEMALE(0);
	
	private final int _gender;
	
	Gender(int g) {
		_gender = g;
	}
	
    public int getGender(){return _gender;}
    public boolean Compare(int i){return _gender == i;}
    public static Gender getValue(int gender) {
    	Gender[] As = Gender.values();
        for(int i = 0; i < As.length; i++) {
            if(As[i].Compare(gender))
                return As[i];
        }
        return Gender.MALE;
    }
}
