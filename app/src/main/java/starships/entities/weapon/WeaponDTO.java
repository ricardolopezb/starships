package starships.entities.weapon;

import org.json.simple.JSONObject;
import starships.entities.bullet.BulletType;


public class WeaponDTO {
        private WeaponType weaponType;
        private BulletType bulletType;
        private ShotType shotType;
        private Integer bulletsPerShot;
        private Double shotSpeed;

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weapon-type", weaponType.name());
        jsonObject.put("bullet-type", bulletType.name());
        jsonObject.put("shot-type", shotType.name());
        jsonObject.put("bullets-per-shot", bulletsPerShot);
        jsonObject.put("shot-speed", shotSpeed);
        return jsonObject;
    }

    public static WeaponDTO fromJson(JSONObject jsonObject){
        WeaponDTO dto = new WeaponDTO();
        dto.setWeaponType(WeaponType.valueOf((String)jsonObject.get("weapon-type")));
        dto.setBulletType(BulletType.valueOf((String)jsonObject.get("bullet-type")));
        dto.setShotType(ShotType.valueOf((String)jsonObject.get("shot-type")));
        dto.setBulletsPerShot(((Long)jsonObject.get("shot-type")).intValue());
        dto.setShotSpeed(((Double)jsonObject.get("shot-type")));
        return dto;
    }



        public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setShotType(ShotType shotType) {
        this.shotType = shotType;
    }

    public void setBulletsPerShot(Integer bulletsPerShot) {
        this.bulletsPerShot = bulletsPerShot;
    }

    public void setShotSpeed(Double shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    public static WeaponDTO fromWeapon(Weapon weapon){
        WeaponDTO dto = new WeaponDTO();
        dto.setBulletsPerShot(weapon.getBulletsPerShot());
        dto.setWeaponType(weapon.getWeaponType());
        dto.setBulletType(weapon.getBulletType());
        dto.setShotType(weapon.getShotType());
        dto.setShotSpeed(weapon.getShotSpeed());
        return dto;
    }



    public WeaponType getWeaponType() {
        return weaponType;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public Integer getBulletsPerShot() {
        return bulletsPerShot;
    }

    public Double getShotSpeed() {
        return shotSpeed;
    }
}
