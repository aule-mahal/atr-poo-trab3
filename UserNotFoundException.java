import java.util.*;
import java.io.*;

public class UserNotFoundException extends Exception
{
	public UserNotFoundException()
	{
		super("Usuario inexistente!");
	}
}