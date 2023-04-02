package server.api;

import commons.Preset;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.PresetService;

import java.util.List;

@RestController
@RequestMapping("/api/presets")
public class PresetController {
    private final PresetService presetService;

    public PresetController(PresetService presetService){
        this.presetService = presetService;
    }

    @GetMapping("/")
    public List<Preset> getAll(){
        return this.presetService.getAllPresets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preset> getById(
            @PathVariable("id") int id){
        Preset found;

        try {
            found = this.presetService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(found);

    }

    @PostMapping("/")
    public ResponseEntity<Preset> add(@RequestBody Preset preset){
        Preset saved;
        try {
            saved = this.presetService.add(preset);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Preset> deleteById(@PathVariable("id") Integer id){
        Preset deletedPreset;
       try{
           deletedPreset = this.presetService.deleteById(id);
       }catch (javax.persistence.PersistenceException e){
           System.out.println("Preset can not be deleted, some cards" +
                   "posses this preset!");
           return ResponseEntity.badRequest().build();
       }catch (Exception e){
           return ResponseEntity.badRequest().build();
       }
       return ResponseEntity.ok(deletedPreset);

    }

    @PutMapping("/{id}/background/{background}")
    public ResponseEntity<Preset> updateBackgroundById(@PathVariable("id") Integer id,
                                                       @PathVariable("background") String background){
        Preset updatedPreset;
        try{
            updatedPreset = this.presetService.editPresetBackgroundById(id, background);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedPreset);
    }
    
    @PutMapping("/id/font/{font}")
    public ResponseEntity<Preset> updateFontById(@PathVariable("id") Integer id,
                                                       @PathVariable("font") String font){
        Preset updatedPreset;
        try{
            updatedPreset = this.presetService.editPresetFontById(id, font);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedPreset);
    }

    
}
