package minechem.handler.message;

import io.netty.buffer.ByteBuf;
import minechem.registry.ResearchRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ResearchMessage implements IMessage
{
    private String key;

    public ResearchMessage() {

    }

    public ResearchMessage(String key)
    {
        this.key = key;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int length = buf.readInt();
        this.key = new String(buf.readBytes(length).array());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.key.length());
        buf.writeBytes(this.key.getBytes());
    }

    public static class Handler extends MessageHandler<ResearchMessage> {
        @Override
        public void handle(ResearchMessage message, MessageContext ctx) {
            ResearchRegistry.getInstance().addResearch(getPlayer(ctx), message.key);
        }
    }
}
