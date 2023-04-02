package server.services;

import commons.Board;
import commons.Card;
import commons.Preset;
import server.api.PresetController;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.PresetRepository;

import javax.transaction.Transactional;
import java.util.List;

public class PresetService {
    private PresetRepository presetRepo;
    private BoardRepository boardRepo;

    private CardRepository cardRepo;

    public PresetService(PresetRepository presetRepository,
                         BoardRepository boardRepo,
                         CardRepository cardRepo){
        this.presetRepo = presetRepository;
        this.boardRepo = boardRepo;
        this.cardRepo = cardRepo;
    }

    public List<Preset> getAllPresets(){
        return this.presetRepo.findAll();
    }

    public Preset getPresetById(int id) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: "+id+" does not exist")
                );
        return preset;
    }

    @Transactional
    public Preset addPreset(Preset preset) throws Exception{
        if(!boardRepo.existsById(preset.boardId))
            throw new Exception("Board does not exist");
        if(preset.id!=null && presetRepo.existsById(preset.id))
            throw new Exception("Preset with id: " + preset.id +" already exists");
        Board board = this.boardRepo.findById(preset.boardId).get();
        board.addCardPreset(preset);
        return presetRepo.save(preset);
    }

    @Transactional
    public Preset removePresetById(int id)throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        presetRepo.deleteById(id);
        return preset;
    }

    @Transactional
    public Preset editPresetBackgroundById(int id, String background) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        preset.backgroundColor= background;
        return presetRepo.save(preset);
    }

    @Transactional
    public Preset editPresetFontById(int id, String font) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        preset.font= font;
        return presetRepo.save(preset);
    }




}
