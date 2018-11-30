package dating.innovative.gameshowdating.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    public String username;
    public String password;
    @SerializedName("profilePicture")
    public String profileImage;
    public String biography;
    public String video1;
    public String video2;
    public String video3;
    public String sex;
    public int age;
    public int match;

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public User(String username, String password, String profileImage, String biography, String video1, String video2, String video3, String sex, int age, int match) {
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.biography = biography;
        this.video1 = video1;
        this.video2 = video2;
        this.video3 = video3;
        this.sex = sex;
        this.age = age;
        this.match = match;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }

    public String getVideo2() {
        return video2;
    }

    public void setVideo2(String video2) {
        this.video2 = video2;
    }

    public String getVideo3() {
        return video3;
    }

    public void setVideo3(String video3) {
        this.video3 = video3;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", biography='" + biography + '\'' +
                ", video1='" + video1 + '\'' +
                ", video2='" + video2 + '\'' +
                ", video3='" + video3 + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", match=" + match +
                '}';
    }
}
