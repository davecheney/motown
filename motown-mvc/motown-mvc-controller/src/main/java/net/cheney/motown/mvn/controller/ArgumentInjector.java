package net.cheney.motown.mvn.controller;

import net.cheney.motown.server.api.Environment;

public abstract class ArgumentInjector {

	public abstract Object[] inject(Environment env);
}
