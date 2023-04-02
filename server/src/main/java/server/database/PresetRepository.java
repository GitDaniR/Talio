package server.database;

import commons.Preset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PresetRepository extends JpaRepository<Preset, Integer> {
    @Modifying
    @Query("UPDATE Preset p SET p.backgroundColor = :color WHERE p.id = :id")
    public void updatePresetBackgroundById(@Param("id") Integer id, @Param("color") String color);

    @Modifying
    @Query("UPDATE Preset p SET p.font = :font WHERE p.id = :id")
    public void updatePresetFontById(@Param("id") Integer id, @Param("font") String font);

}
