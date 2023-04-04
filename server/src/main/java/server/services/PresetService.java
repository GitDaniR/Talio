package server.services;

import commons.Board;
import commons.Preset;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.PresetRepository;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;
import java.util.List;
@Service
public class PresetService {
    private PresetRepository presetRepo;
    private BoardRepository boardRepo;

    public PresetService(PresetRepository presetRepository,
                         BoardRepository boardRepo){
        this.presetRepo = presetRepository;
        this.boardRepo = boardRepo;
    }

    public List<Preset> getAllPresets(){
        return this.presetRepo.findAll();
    }

    public Preset getById(int id) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: "+id+" does not exist")
                );
        return preset;
    }

    @Transactional
    public Preset add(Preset preset) throws Exception{
        if(!boardRepo.existsById(preset.boardId))
            throw new Exception("Board does not exist");
        if(preset.id!=null && presetRepo.existsById(preset.id))
            throw new Exception("Preset with id: " + preset.id +" already exists");
        Board board = this.boardRepo.findById(preset.boardId).get();
        board.addCardPreset(preset);
        return presetRepo.save(preset);
    }

    @Transactional
    public Preset deleteById(int id)throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        presetRepo.deleteById(id);
        return preset;
    }

    @Transactional
    public String editPresetBackgroundById(int id, String background) throws Exception{
        if(id<0 || !presetRepo.existsById(id)){
            throw new Exception("Invalid id");
        }
        if(background == null){
            throw new Exception("Invalid background");
        }
        presetRepo.updatePresetBackgroundById(id, background);
        return "Preset font has been updated successfully.";
    }

    @Transactional
    public String editPresetFontById(int id, String font) throws Exception{
        if(id<0 || !presetRepo.existsById(id)){
            throw new Exception("Invalid id");
        }
        if(font == null){
            throw new Exception("Invalid font");
        }
        presetRepo.updatePresetFontById(id, font);
        return "Preset font has been updated successfully.";
    }
    @TransactionScoped
    public Preset setAsDefault(int id) throws Exception{

        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );

        Board board = boardRepo.findById(preset.boardId).get();
        board.defaultCardPreset = preset;
        boardRepo.save(board);
        return presetRepo.save(preset);

    }


}
