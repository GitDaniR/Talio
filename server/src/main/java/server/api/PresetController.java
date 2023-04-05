package server.api;

import commons.Preset;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.PresetService;

import java.util.List;

@RestController
@RequestMapping("/api/presets")
public class PresetController {
    private final PresetService presetService;
    private SimpMessagingTemplate msgs;

    public PresetController(PresetService presetService, SimpMessagingTemplate msgs){
        this.msgs = msgs;
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
            if(msgs!=null)
                msgs.convertAndSend("/topic/customization/presets",1.0);
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
            if(msgs!=null)
                msgs.convertAndSend("/topic/customization/presets",1.0);
        } catch (javax.persistence.PersistenceException e){
            System.out.println("Preset can not be deleted, some cards" +
                   "posses this preset!");
            return ResponseEntity.badRequest().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deletedPreset);

    }

    @PutMapping("/{id}/background/{background}")
    public ResponseEntity<String> updateBackgroundById(@PathVariable("id") Integer id,
                                                       @PathVariable("background") String
                                                               background){
        try{
            ResponseEntity response =
                    ResponseEntity.ok(this.presetService.editPresetBackgroundById(id, background));
            if(msgs!=null)
                msgs.convertAndSend("/topic/customization/presets",1.0);
            return response;
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    
    @PutMapping("/{id}/font/{font}")
    public ResponseEntity<String> updateFontById(@PathVariable("id") Integer id,
                                                       @PathVariable("font") String font){
        try{
            ResponseEntity response =
                    ResponseEntity.ok(this.presetService.editPresetFontById(id, font));
            if(msgs!=null)
                msgs.convertAndSend("/topic/customization/presets",1.0);
            return response;
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}/default")
    public ResponseEntity<Preset> setDefault(@PathVariable("id") Integer id){
        Preset defaultPreset;
        try{
            defaultPreset = this.presetService.setAsDefault(id);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(defaultPreset);
    }

}
