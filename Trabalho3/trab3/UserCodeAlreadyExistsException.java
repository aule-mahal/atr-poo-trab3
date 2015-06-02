package trab3;

import java.util.*;
import java.io.*;

public class UserCodeAlreadyExistsException extends Exception
{
	public UserCodeAlreadyExistsException()
	{
		super("Código de usuário já exsitente!");
	}
}
