package ma.sid.services;

import ma.sid.dao.SocieteRepository;
import ma.sid.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocieteServiceImpl implements SocieteService {

    @Autowired
    SocieteRepository societeRepository;

    @Override
    public List<Societe> recupererSocietes() {
        return societeRepository.findAll();
    }
}
