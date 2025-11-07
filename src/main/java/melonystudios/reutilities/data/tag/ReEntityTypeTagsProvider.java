package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.entity.ReEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public class ReEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ReEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(output, registries, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Entity Type Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Common tags
        this.tag(Tags.EntityTypes.BOATS).add(ReEntities.BOAT.get(), ReEntities.CHEST_BOAT.get());
    }
}
