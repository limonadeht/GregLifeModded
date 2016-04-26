package greglife.util;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class GregLifeLexicon extends LexiconEntry implements IAddonEntry{

	public GregLifeLexicon(String name, LexiconCategory category){
        super(name, category);
        BotaniaAPI.addEntry(this, category);
    }
    @Override
    public String getSubtitle(){
        return "[GregLife-Server]";
    }
    @Override
    public String getUnlocalizedName() {
        return "avaritia.lexicon." + super.getUnlocalizedName();
    }
}
